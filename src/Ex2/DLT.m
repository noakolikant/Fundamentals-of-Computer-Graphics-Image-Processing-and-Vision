function [ H ] = DLT( matches )
%DLT Computes direct linear transform
    rhs_point = matches(:, 1:2);
    lhs_point = matches(:, 3:4);
    
    rhs_hom_point = [rhs_point ones(size(rhs_point,1), 1)]; % convert to home. coordinates
    lhs_hom_point = [lhs_point ones(size(lhs_point,1), 1)]; % convert to home. coordinates
    
    % Normalize points - Do it by matrix (homg. matrix)
    rhs_avgs = mean(rhs_point);
    lhs_avgs = mean(lhs_point);
    
    rhs_t_translate = [1, 0 , -rhs_avgs(1); 0, 1, -rhs_avgs(2); 0, 0, 1];
    lhs_t_translate = [1, 0 , -lhs_avgs(1); 0, 1, -lhs_avgs(2); 0, 0, 1];
    
    rhs_hom_point = rhs_t_translate * rhs_hom_point';
    lhs_hom_point = lhs_t_translate * lhs_hom_point';
    
    rhs_distance_vector = sqrt(rhs_hom_point(1,:).^2 + rhs_hom_point(2,:).^2);
    lhs_distance_vector = sqrt(lhs_hom_point(1,:).^2 + lhs_hom_point(2,:).^2);
    rhs_mean_distance = mean(rhs_distance_vector(:));
    lhs_mean_distance = mean(lhs_distance_vector(:));
    
    rhs_scale_factor = sqrt(2) / rhs_mean_distance;
    lhs_scale_factor = sqrt(2) / lhs_mean_distance;
    
    rhs_t_scale = [rhs_scale_factor 0 0; 0 rhs_scale_factor 0; 0 0 1];
    lhs_t_scale = [lhs_scale_factor 0 0; 0 lhs_scale_factor 0; 0 0 1];
    
    rhs_hom_point = rhs_t_scale * rhs_hom_point;
    lhs_hom_point = lhs_t_scale * lhs_hom_point;    
    
    
    

end
