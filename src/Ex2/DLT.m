function [ H ] = DLT( matches )
%DLT Computes direct linear transform
    rhs_point = matches(:, 1:2);
    lhs_point = matches(:, 3:4);
    
    rhs_hom_point = [rhs_point ones(size(rhs_point,1), 1)]; % convert to home. coordinates
    lhs_hom_point = [lhs_point ones(size(lhs_point,1), 1)]; % convert to home. coordinates
    
    [rhs_hom_point, rhs_T] = Normalize2dHomPoints(rhs_hom_point);
    [lhs_hom_point, lhs_T] = Normalize2dHomPoints(lhs_hom_point);

    % Compute Ai
    A = [];
    x_lhs = lhs_hom_point(1,:);
    y_lhs = lhs_hom_point(2,:);
    w_lhs = lhs_hom_point(3,:);
    for i=1:size(rhs_hom_point,2)
        A = [A; ...
            zeros(3,1)', -w_lhs(i)*rhs_hom_point(:,i)', ...
            y_lhs(i)*rhs_hom_point(:,i)';  ...
            w_lhs(i)*rhs_hom_point(:,i)', zeros(3,1)', ...
            -x_lhs(i)*rhs_hom_point(:,i)'];
    end
    
    [U, D, V] = svd(A);
    
    h = reshape(V(:,9),3,3);
    H = inv(lhs_T) * h * rhs_T;
    H = H';
    
end

